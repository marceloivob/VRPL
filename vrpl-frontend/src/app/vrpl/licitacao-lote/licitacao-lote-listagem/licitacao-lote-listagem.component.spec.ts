import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LicitacaoLoteListagemComponent } from './licitacao-lote-listagem.component';

describe('LicitacaoLoteListagemComponent', () => {
  let component: LicitacaoLoteListagemComponent;
  let fixture: ComponentFixture<LicitacaoLoteListagemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LicitacaoLoteListagemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LicitacaoLoteListagemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
