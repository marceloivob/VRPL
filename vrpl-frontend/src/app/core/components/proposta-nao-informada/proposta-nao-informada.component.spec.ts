import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropostaNaoInformadaComponent } from './proposta-nao-informada.component';

describe('PropostaNaoEncontradaComponent', () => {
  let component: PropostaNaoInformadaComponent;
  let fixture: ComponentFixture<PropostaNaoInformadaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PropostaNaoInformadaComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropostaNaoInformadaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
