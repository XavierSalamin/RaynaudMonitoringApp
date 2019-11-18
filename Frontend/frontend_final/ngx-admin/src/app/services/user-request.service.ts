import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserRequestService {

  public serverUrl: string = '//localhost:';
  //public serverUrl: string = '//193.54.74.69:';
  public serverPort: string = '8080/';

  constructor(private http: HttpClient) {
  }

  headers = new HttpHeaders({
    'Content-Type': 'application/json',
  });

  getAll(): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'user-request/find/all');
  }


  activateUser(firstname): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'user-request/activate/' + firstname);
  }

  getAllProfile(): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'user-request/profile/all');
  }

  getPicture(id: string, picId: string): Observable<Blob> {
    return this.http.get(this.serverUrl + '4984/staging/' + id + '/blob_%2F' + picId, {responseType: 'blob'});
  }

  postUserProfile(data): Observable<any> {
    return this.http.post(this.serverUrl + this.serverPort +
      'user-request/add', JSON.stringify(data), {headers: this.headers});
  }

  getUserRequest(firstname): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'user-request/find/' + firstname);
  }

  getUserProfile(id): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'user-request/profile/' + id);
  }

  getAllCrisis(): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'crisis/find/all');
  }

  getCrisisById(id): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'crisis/find/' + id);
  }


  getExcelById(patientNumber): Observable<any> {
    return this.http.get(this.serverUrl + this.serverPort + 'crisis/excel/' + patientNumber,
      {responseType: 'arraybuffer'});
  }

}
