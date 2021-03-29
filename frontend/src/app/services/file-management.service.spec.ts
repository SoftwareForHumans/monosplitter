import { TestBed } from '@angular/core/testing';

import { FileManagementService } from './file-management.service';

describe('FileManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FileManagementService = TestBed.get(FileManagementService);
    expect(service).toBeTruthy();
  });
});
