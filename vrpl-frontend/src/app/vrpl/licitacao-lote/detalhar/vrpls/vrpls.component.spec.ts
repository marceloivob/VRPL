import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VrplsComponent } from './vrpls.component';

describe('VrplsComponent', () => {
  let component: VrplsComponent;
  let fixture: ComponentFixture<VrplsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VrplsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VrplsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
