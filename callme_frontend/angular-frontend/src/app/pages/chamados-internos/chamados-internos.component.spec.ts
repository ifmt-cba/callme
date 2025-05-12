import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChamadosInternosComponent } from './chamados-internos.component';

describe('ChamadosInternosComponent', () => {
  let component: ChamadosInternosComponent;
  let fixture: ComponentFixture<ChamadosInternosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChamadosInternosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChamadosInternosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
