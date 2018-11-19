import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubOverviewComponent } from './sub-overview.component';

describe('SubOverviewComponent', () => {
  let component: SubOverviewComponent;
  let fixture: ComponentFixture<SubOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
