import { TestBed } from '@angular/core/testing';

import { UserRequestService } from './user-request.service';

describe('UserRequestService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserRequestService = TestBed.get(UserRequestService);
    expect(service).toBeTruthy();
  });
});
