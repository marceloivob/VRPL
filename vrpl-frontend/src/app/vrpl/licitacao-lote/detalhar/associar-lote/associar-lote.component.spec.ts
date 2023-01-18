// import { TestStore } from '@testing/utils';

import { APP_BASE_HREF, CommonModule } from '@angular/common';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NgxsModule } from '@ngxs/store';
import { AssociarLoteComponent } from './associar-lote.component';
import { PropostaNaoInformadaComponent } from 'src/app/core/components/proposta-nao-informada/proposta-nao-informada.component';
import { PageNotFoundComponent } from 'src/app/core/components/page-not-found/page-not-found.component';
import { LicitacaoLoteListagemComponent } from '../../licitacao-lote-listagem/licitacao-lote-listagem.component';
import { LicitacaoState } from 'src/app/model/licitacao/licitacao.state';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { LicitacaoLoteRoutingModule } from '../../licitacao-lote-routing.module';
import { SiconvModule } from 'src/app/siconv/siconv.module';




describe('AssociarLoteComponent', () => {
  let component: AssociarLoteComponent;
  let fixture: ComponentFixture<AssociarLoteComponent>;

  // let store: Store;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AssociarLoteComponent,
        PropostaNaoInformadaComponent,
        PageNotFoundComponent,
        // APP_BASE_HREF,
        LicitacaoLoteListagemComponent

      ],
      imports: [
        // FormsModule,
        // SiconvModule,
        NgxsModule.forRoot([LicitacaoState]),
        NgxsModule.forRoot(),
        AppRoutingModule,
        // APP_BASE_HREF,
        CommonModule,
        FormsModule,
        LicitacaoLoteRoutingModule,
        SiconvModule
      ],
      providers: [
        HttpClient,
        HttpHandler,
        { provide: APP_BASE_HREF, useValue: '/' },
        AssociarLoteComponent,
        // { provide: Store, useClass: TestStore }

      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssociarLoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

});
