import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DatabaseItemComponent} from './database-item.component';

describe('DatabaseItemComponent', () => {
  let component: DatabaseItemComponent;
  let fixture: ComponentFixture<DatabaseItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DatabaseItemComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatabaseItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
