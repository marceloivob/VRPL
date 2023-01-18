import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VrplComponent } from './vrpl.component';

describe('VrplComponent', () => {
  let component: VrplComponent;
  let fixture: ComponentFixture<VrplComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VrplComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VrplComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
