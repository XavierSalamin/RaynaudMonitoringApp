import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class UserRequestService {

constructor(private http: HttpClient) {
   }
     headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });
     getAll(): Observable<any> {
    return this.http.get('//localhost:8080/user-request/find/all');
	}


   activateUser(firstname): Observable<any>{
     return this.http.get('//localhost:8080/user-request/activate/'+firstname);
   }

   getAllProfile(): Observable<any> {
    return this.http.get('//localhost:8080/user-request/profile/all');
  }

         getPicture(): Observable<Blob> {
    return this.http.get('//localhost:4984/staging/allez/blob_1', { responseType: 'blob' });
  }

  postUserProfile(data): Observable<any> {
    return this.http.post('//localhost:8080/user-request/add', JSON.stringify(data), {headers : this.headers} );
  }

   getUserRequest(firstname): Observable<any>{
     return this.http.get('//localhost:8080/user-request/find/'+firstname);
   }
  getUserProfile(id): Observable<any>{
     return this.http.get('//localhost:8080/user-request/profile/'+id);
   }
     getAllCrisis(): Observable<any> {
    return this.http.get('//localhost:8080/crisis/find/all');
  }

       getCrisisById(id): Observable<any> {
    return this.http.get('//localhost:8080/crisis/find/'+id);
  }


    getExcelById(patientNumber): Observable<any> {
    return this.http.get('//localhost:8080/crisis/excel/'+patientNumber);
  }

}
