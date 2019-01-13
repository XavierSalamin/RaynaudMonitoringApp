import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserRequestListComponent } from './user-request-list.component';

describe('UserRequestListComponent', () => {
  let component: UserRequestListComponent;
  let fixture: ComponentFixture<UserRequestListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserRequestListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserRequestListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
