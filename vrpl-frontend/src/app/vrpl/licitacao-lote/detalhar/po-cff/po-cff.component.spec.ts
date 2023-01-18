import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PoCffComponent } from './po-cff.component';

describe('PoCffComponent', () => {
  let component: PoCffComponent;
  let fixture: ComponentFixture<PoCffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PoCffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PoCffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
