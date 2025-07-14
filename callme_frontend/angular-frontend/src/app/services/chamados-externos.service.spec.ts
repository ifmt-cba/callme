import { TestBed } from '@angular/core/testing';

import { ChamadoExternoService } from './chamados-externos.service';

describe('ChamadoExternoService', () => {
  let service: ChamadoExternoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChamadoExternoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
