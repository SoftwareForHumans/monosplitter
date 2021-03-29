import { Injectable } from '@angular/core';
import { backendSettings } from '../utils/backendSettings'
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileManagementService {
  backend: backendSettings = new backendSettings();
  SERVER_URL: string = `${this.backend.localhost}/file-management`;

  constructor(private http:HttpClient) { }

  /** POST: upload file on server side */
  uploadFile(file:File): Observable<any>{
    let formData = new FormData();
    formData.append('file', file); 
    return this.http.post<any>(`${this.SERVER_URL}/upload`, formData)
      .pipe(
        catchError(this.handleError<any>('uploadFile'))
      )
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
   private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
