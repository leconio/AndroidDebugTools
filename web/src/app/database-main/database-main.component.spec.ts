import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DatabaseMainComponent} from './database-main.component';

describe('DatabaseMainComponent', () => {
  let component: DatabaseMainComponent;
  let fixture: ComponentFixture<DatabaseMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DatabaseMainComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatabaseMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
