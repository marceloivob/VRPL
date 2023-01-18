import { Component, OnInit, OnDestroy } from '@angular/core';
import { AppState } from '../../../model/app/app.state';
import { Select } from '@ngxs/store';
import { Observable, Subscription } from 'rxjs';
import { AppStateModel } from '../../../model/app/app.state.model';

@Component({
  selector: 'vrpl-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit, OnDestroy {

  @Select(AppState) propostaState$: Observable<AppStateModel>;
  private stateSubscription: Subscription;

  exibeSpinner = false;

  constructor() { }

  ngOnInit() {
    this.stateSubscription =
      this.propostaState$.subscribe( (app) => this.exibeSpinner = app.backgroundRequest > 0 );
  }

  ngOnDestroy() {
    this.stateSubscription.unsubscribe();
  }

}
