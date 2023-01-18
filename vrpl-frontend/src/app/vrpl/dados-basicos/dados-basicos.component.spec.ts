import { HttpClient, HttpHandler } from '@angular/common/http';
import { NgxsModule } from '@ngxs/store';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DadosBasicosComponent } from './dados-basicos.component';
import { PropostaState } from '../../model/proposta/proposta.state';
import { PropostaStateModel } from '../../model/proposta/proposta.state.model';



describe('DadosBasicosComponent', () => {
  let component: DadosBasicosComponent;
  let fixture: ComponentFixture<DadosBasicosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DadosBasicosComponent ],
      imports: [ 
        NgxsModule.forRoot([PropostaState])
      ],
      providers: [ HttpClient, HttpHandler ]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DadosBasicosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });  

  
  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */

  /*it('Deve retornar os dados básicos da proposta', () => {
    const proposta: PropostaStateModel = {
      idProposta: 1,
      numeroProposta: 'string',
      anoProposta: 'string',
      valorGlobal: 10,
      valorRepasse: 10,
      valorContrapartida: 10,
      modalidade: 1, //deve ser 1
      descricaoModalidade: 'string',
      objeto: 'string',
      numeroConvenio: 'string',
      anoConvenio: 'string',
      dataAssinaturaConvenio: new Date('24/10/2018'),
      codigoPrograma: 'string',
      nomePrograma: 'string',
      identificadorDoProponente: 'string',
      nomeProponente: 'string',
      uf: 'string',
      percentualMinimoContrapartida: 2,
      nomeMandataria: 'string',
      categoria: 'string',
      nivelContrato: 'string',
      apelidoDoEmpreendimento: 'string',
      situacaoAcffo: 'string',
      spaHomologado: true
    };

    fixture.componentInstance.proposta = proposta;
    expect(fixture.componentInstance.isPropostaDoTipoConvenio()).toBeTruthy();
  });*/

});
