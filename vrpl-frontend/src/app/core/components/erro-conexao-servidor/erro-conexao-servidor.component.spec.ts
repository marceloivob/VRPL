import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ErroConexaoServidorComponent } from './erro-conexao-servidor.component';



describe('ErroServidorSpaHomologadoComponent', () => {
  let component: ErroConexaoServidorComponent;
  let fixture: ComponentFixture<ErroConexaoServidorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ErroConexaoServidorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ErroConexaoServidorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
