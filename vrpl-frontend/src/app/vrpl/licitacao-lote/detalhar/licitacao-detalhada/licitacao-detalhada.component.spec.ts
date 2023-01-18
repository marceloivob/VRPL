import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LicitacaoDetalhadaComponent } from './licitacao-detalhada.component';

describe('LicitacaoDetalhadaComponent', () => {
  let component: LicitacaoDetalhadaComponent;
  let fixture: ComponentFixture<LicitacaoDetalhadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LicitacaoDetalhadaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LicitacaoDetalhadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
