import { types, flow, SnapshotIn } from 'mobx-state-tree';
import { store } from './Store';
import { getUserSession, loginRequest, logoutRequest } from '../api';

export const App = types
  .model('App', {
    language: types.string,
    loggedIn: types.boolean,
    loginError: types.string,
    countUnconfirmed: types.number,
    email: types.string,
    dayCaption: types.string,
    week: types.string,
    weekCaption: types.string,
  })
  .actions((self) => {
    const logIn = flow(function* logIn(email: string, password: string) {
      self.loginError = '';
      let result;

      try {
        const { data } = yield loginRequest(email, password);

        if (!data.loginError) {
          setInitialData(data);
        } else {
          setResponseError(data.loginError);
        }
        result = { ...data };
      } catch (error) {
        setResponseError(error);
        result = {
          loginError: error,
        };
      }

      return result;
    });

    const logOut = flow(function* logOut() {
      yield logoutRequest();

      self.loggedIn = false;
    });

    const getSession = flow(function* getSession() {
      let response;
      try {
        response = yield getUserSession();
        setInitialData(response.data);
        // update in the store also the current week
        const { week, weekCaption } = response.data;
        week && store.week.changeCurrentWeek(week);
        weekCaption && store.week.changeCaption(weekCaption);
      } catch (e) {
        // 401 (Unauthorized)
      }

      return response;
    });

    const setInitialData = function setInitialData(dataToSet: Partial<SnapshotIn<typeof self>>) {
      Object.assign(self, dataToSet);
    };

    const setResponseError = function setResponseError(errorMsg) {
      self.loginError = errorMsg;
    };

    return {
      logIn,
      logOut,
      getUserSession: getSession,
      setInitialData,
      setResponseError,
    };
  });
