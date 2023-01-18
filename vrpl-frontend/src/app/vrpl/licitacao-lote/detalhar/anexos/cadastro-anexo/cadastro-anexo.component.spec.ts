import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroAnexoComponent } from './cadastro-anexo.component';

describe('CadastroAnexoComponent', () => {
  let component: CadastroAnexoComponent;
  let fixture: ComponentFixture<CadastroAnexoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadastroAnexoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroAnexoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
