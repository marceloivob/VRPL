import { HttpClient, HttpHandler } from '@angular/common/http';
import { QciState } from '../../model/qci/qci.state';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QciExternoComponent } from './qci-externo.component';
import { NgxsModule } from '@ngxs/store';

describe('QciExternoComponent', () => {
  let component: QciExternoComponent;
  let fixture: ComponentFixture<QciExternoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QciExternoComponent ],
      imports: [        
        NgxsModule.forRoot([QciState])
        
      ],
      providers: [ HttpClient, HttpHandler ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QciExternoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
