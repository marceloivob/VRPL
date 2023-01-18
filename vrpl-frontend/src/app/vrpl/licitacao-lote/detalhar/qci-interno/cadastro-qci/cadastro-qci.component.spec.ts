import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroQciComponent } from './cadastro-qci.component';

describe('CadastroQciComponent', () => {
  let component: CadastroQciComponent;
  let fixture: ComponentFixture<CadastroQciComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadastroQciComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroQciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
