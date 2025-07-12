import { ComponentFixture, TestBed } from '@angular/core/testing';


import { ChamadoDoUsuarioComponent } from './chamado-do-usuario.component';

describe('ChamadoDoUsuarioComponent', () => {
  let component: ChamadoDoUsuarioComponent;
  let fixture: ComponentFixture<ChamadoDoUsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChamadoDoUsuarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChamadoDoUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
