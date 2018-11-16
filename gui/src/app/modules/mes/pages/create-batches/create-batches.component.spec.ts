import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBatchesComponent } from './create-batches.component';

describe('CreateBatchesComponent', () => {
  let component: CreateBatchesComponent;
  let fixture: ComponentFixture<CreateBatchesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateBatchesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateBatchesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
