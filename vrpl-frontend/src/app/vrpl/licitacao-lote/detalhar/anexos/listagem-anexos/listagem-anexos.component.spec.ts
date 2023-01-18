import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ListagemAnexosComponent } from './listagem-anexos.component';



describe('ListagemAnexosComponent', () => {
  let component: ListagemAnexosComponent;
  let fixture: ComponentFixture<ListagemAnexosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ListagemAnexosComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListagemAnexosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
