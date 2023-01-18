import { OnInit, OnDestroy } from '@angular/core';
import { Subject, MonoTypeOperatorFunction } from 'rxjs';
import { Store } from '@ngxs/store';
import { takeUntil } from 'rxjs/operators';


export abstract class BaseComponent implements OnInit, OnDestroy {

  private unsubscribe$ = new Subject();
  private loaded$ = false;

  constructor(
    protected readonly store: Store
  ) { }

  ngOnInit() {
    const actions  = this.loadActions();

    if ((Array.isArray(actions) && actions.length > 0) || actions) {
      this.store.dispatch(actions)
        .pipe(this.takeUntilDestroyed())
        .subscribe(() => {
          this.loaded$ = true;
          this.onLoad();
        });
    } else {
      this.loaded$ = true;
    }

    this.init();
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
    this.destroy();
  }

  takeUntilDestroyed<T>(): MonoTypeOperatorFunction<T> {
    return takeUntil(this.unsubscribe$);
  }

  get loaded() {
    return this.loaded$;
  }

  loadActions(): any | any[] {
    return null;
  }

  dispatch(actions: any | any[]) {
    return this.store.dispatch(actions);
  }

  abstract init(): void;

  destroy() { }

  onLoad() { }

}
