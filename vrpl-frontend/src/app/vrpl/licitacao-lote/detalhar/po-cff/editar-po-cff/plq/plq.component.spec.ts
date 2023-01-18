import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlqComponent } from './plq.component' ;

describe('PlqComponent', () => {
  let component: PlqComponent;
  let fixture: ComponentFixture<PlqComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlqComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlqComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
