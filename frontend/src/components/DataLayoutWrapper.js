import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';

import { patchViewAttributes } from '../actions/ViewAttributesActions';
import { parseToDisplay } from '../utils/documentListHelper';

import SelectionAttributes from './app/SelectionAttributes';

/**
 * @file Class based component.
 * @module DocumentList
 * @extends PureComponent
 */
export default class DataLayoutWrapper extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      layout: [],
      data: [],
      dataId: null,
    };
  }

  componentDidMount = () => {
    this.mounted = true;
  };

  componentWillUnmount = () => {
    this.mounted = false;
  };

  handleChange = (field, value) => {
    this.setState({
      data: {
        ...this.state.data,
        [field]: {
          ...this.state.data[field],
          value,
        },
      },
    });
  };

  handlePatch = (prop, value, cb) => {
    const { windowId, viewId } = this.props;
    const { dataId: rowId } = this.state;

    /*eslint-disable */
    patchViewAttributes(windowId, viewId, rowId, prop, value).then(({ data }) => {
      if (data.length) {
        const preparedData = parseToDisplay(data[0].fieldsByName);
        
        if (preparedData) {
          Object.keys(preparedData).map(key => {
            this.setState({
              data: {
                ...this.state.data,
                [key]: {
                  ...this.state.data[key],
                  ...preparedData[key],
                }
              }
            });
          });
        }
      }

      cb && cb();
    });
    /*eslint-enable */
  };

  setData = (data, dataId, cb) => {
    const preparedData = parseToDisplay(data);
    this.mounted &&
      this.setState(
        {
          data: preparedData,
          dataId,
        },
        cb
      );
  };

  setLayout = (layout, cb) => {
    this.mounted &&
      this.setState(
        {
          layout,
        },
        cb
      );
  };

  render() {
    const { layout, data } = this.state;
    const { className } = this.props;

    // sometimes it's a number, and React complains about wrong type
    const dataId = this.state.dataId + '';

    return (
      <div className={className}>
        <SelectionAttributes
          {...this.props}
          DLWrapperData={data}
          DLWrapperDataId={dataId}
          DLWrapperLayout={layout}
          DLWrapperSetData={this.setData}
          DLWrapperSetLayout={this.setLayout}
          DLWrapperHandleChange={this.handleChange}
          DLWrapperHandlePatch={this.handlePatch}
        />
      </div>
    );
  }
}

DataLayoutWrapper.propTypes = {
  windowId: PropTypes.string,
  viewId: PropTypes.string,
  children: PropTypes.node,
  className: PropTypes.string,
};
