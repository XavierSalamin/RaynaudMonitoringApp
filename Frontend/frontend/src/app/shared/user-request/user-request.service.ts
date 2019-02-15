import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable({
  providedIn: 'root'
})
export class UserRequestService {

  constructor(private http: HttpClient) {
   }
     getAll(): Observable<any> {
    return this.http.get('//localhost:8080/user-request/find/all');
	}
         getPicture(): Observable<Blob> {
    return this.http.get('//localhost:4984/staging/allez/blob_1', { responseType: 'blob' });
  }
}