import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ErroCarregamentoPropostaComponent } from './erro-carregamento-proposta.component';

describe('ErroCarregamentoPropostaComponent', () => {
  let component: ErroCarregamentoPropostaComponent;
  let fixture: ComponentFixture<ErroCarregamentoPropostaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ErroCarregamentoPropostaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErroCarregamentoPropostaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
