import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropostaEstaSendoImportadaComponent } from './proposta-esta-sendo-importada.component';

describe('PropostaSemDadosImportadosComponent', () => {
  let component: PropostaEstaSendoImportadaComponent;
  let fixture: ComponentFixture<PropostaEstaSendoImportadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropostaEstaSendoImportadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropostaEstaSendoImportadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
