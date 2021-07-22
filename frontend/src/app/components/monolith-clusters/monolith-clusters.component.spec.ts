import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonolithClusters } from './monolith-clusters.component';

describe('SystemModelComponent', () => {
  let component: MonolithClusters;
  let fixture: ComponentFixture<MonolithClusters>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonolithClusters ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonolithClusters);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
