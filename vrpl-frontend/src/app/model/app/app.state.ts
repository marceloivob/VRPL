import { State, Action, StateContext } from '@ngxs/store';
import { HTTPResponse, HTTPRequest } from './app.actions';
import { AppStateModel } from './app.state.model';

@State<AppStateModel>({
  name: 'app',
  defaults: {
    backgroundRequest: 0
  }
})
export class AppState {

  @Action(HTTPResponse)
  httpResponse({ patchState, getState }: StateContext<AppStateModel>) {
    patchState({
      backgroundRequest: getState().backgroundRequest - 1
    });
  }

  @Action(HTTPRequest)
  httpRequest({ patchState, getState }: StateContext<AppStateModel>) {
    patchState({
      backgroundRequest: getState().backgroundRequest + 1
    });
  }

}
