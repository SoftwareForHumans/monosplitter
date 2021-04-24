import { TestBed } from '@angular/core/testing';

import { ProjectAnalysisService } from './project-analysis.service';

describe('ProjectAnalysisService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProjectAnalysisService = TestBed.get(ProjectAnalysisService);
    expect(service).toBeTruthy();
  });
});
