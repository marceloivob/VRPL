import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QciInternoComponent } from './qci-interno.component';

describe('QciInternoComponent', () => {
  let component: QciInternoComponent;
  let fixture: ComponentFixture<QciInternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QciInternoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QciInternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
