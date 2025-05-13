import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChamadosExternosComponent } from './chamados-externos.component';

describe('ChamadosExternosComponent', () => {
  let component: ChamadosExternosComponent;
  let fixture: ComponentFixture<ChamadosExternosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChamadosExternosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ChamadosExternosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
