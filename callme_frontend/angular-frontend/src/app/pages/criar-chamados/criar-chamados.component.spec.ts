import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CriarChamadosComponent } from './criar-chamados.component';

describe('CriarChamadosComponent', () => {
  let component: CriarChamadosComponent;
  let fixture: ComponentFixture<CriarChamadosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CriarChamadosComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CriarChamadosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
