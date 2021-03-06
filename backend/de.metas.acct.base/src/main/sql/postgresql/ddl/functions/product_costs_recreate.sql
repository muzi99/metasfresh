DROP FUNCTION IF EXISTS "de_metas_acct".product_costs_recreate(numeric)
;
DROP FUNCTION IF EXISTS "de_metas_acct".product_costs_recreate(numeric, char(1), numeric)
;

CREATE OR REPLACE FUNCTION "de_metas_acct".product_costs_recreate(p_M_Product_ID  numeric = NULL,
                                                                  p_ReorderDocs   char(1) = 'Y',
                                                                  p_M_Product_IDs numeric[] = NULL)
    RETURNS text
AS
$BODY$
DECLARE
    v_productIds numeric[];
    rowcount     integer;
    v_result     text := '';
BEGIN
    IF (p_M_Product_ID IS NOT NULL AND p_M_Product_ID > 0) THEN
        v_productIds := ARRAY [p_M_Product_ID];
        -- RAISE EXCEPTION 'Product shall be > 0 but it was %', p_M_Product_ID;
    ELSE
        v_productIds := p_M_Product_IDs;
    END IF;

    IF (v_productIds IS NULL OR array_length(v_productIds, 1) <= 0) THEN
        RAISE EXCEPTION
            'No products provided. p_M_Product_ID(=%) or p_M_Product_IDs(=%) shall be set',
            p_M_Product_ID, p_M_Product_IDs;
    END IF;

    v_result := v_result || array_length(v_productIds, 1) || ' products; ';

    DELETE
    FROM m_cost c
    WHERE c.m_product_id = ANY (v_productIds)
    -- AND NOT exists(SELECT 1 FROM m_costelement ce WHERE ce.m_costelement_id = c.m_costelement_id AND ce.costingmethod = 'S') -- exclude standard costing
    ;
    GET DIAGNOSTICS rowcount = ROW_COUNT;
    v_result := v_result || rowcount || ' M_Cost(s) deleted; ';

    DELETE
    FROM m_costdetail
    WHERE m_product_id = ANY (v_productIds);
    GET DIAGNOSTICS rowcount = ROW_COUNT;
    v_result := v_result || rowcount || ' M_CostDetail(s) deleted; ';

    DELETE
    FROM pp_order_cost poc
    WHERE exists(
                  SELECT 1
                  FROM pp_order o
                  WHERE o.pp_order_id = poc.pp_order_id
                    AND o.m_product_id = ANY (v_productIds)
              );
    GET DIAGNOSTICS rowcount = ROW_COUNT;
    v_result := v_result || rowcount || ' PP_Order_Cost(s) deleted; ';


    SELECT count(1)
    INTO rowcount
    FROM (
             SELECT "de_metas_acct".fact_acct_unpost(t.tablename, t.record_id, 'Y')
             FROM (
                      SELECT DISTINCT tablename,
                                      record_id,
                                      m_product_id,
                                      dateacct,
                                      tablename_prio,
                                      least(record_id, reversal_id)
                      FROM "de_metas_acct".accountable_docs_and_lines_v
                      WHERE m_product_id = ANY (v_productIds)
                      ORDER BY m_product_id,
                               dateacct,
                               tablename_prio,
                               least(record_id, reversal_id),
                               record_id
                  ) t
         ) t;
    v_result := v_result || rowcount || ' document(s) unposted; ';

    IF (p_ReorderDocs = 'Y') THEN
        PERFORM "de_metas_acct".accounting_docs_to_repost_reorder();
        v_result := v_result || 'reordered enqueued docs';
    END IF;

    RETURN v_result;
END;
$BODY$
    LANGUAGE plpgsql VOLATILE
                     COST 100
;
