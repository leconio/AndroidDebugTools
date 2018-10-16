import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatabaseSiderComponent } from './database-sider.component';

describe('DatabaseSiderComponent', () => {
  let component: DatabaseSiderComponent;
  let fixture: ComponentFixture<DatabaseSiderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatabaseSiderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatabaseSiderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
