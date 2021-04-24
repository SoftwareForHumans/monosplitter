import { Injectable } from '@angular/core';
import { BackendSettings } from '../utils/BackendSettings';
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectAnalysisService {
  backend: BackendSettings = new BackendSettings();
  SERVER_URL: string = `${this.backend.localhost}/analysis`;

  constructor(private http:HttpClient) { }

   /** POST: execute static analysis */
   staticAnalysis(): Observable<any>{
    return this.http.post<any>(`${this.SERVER_URL}/static`, null)
      .pipe(
        catchError(this.handleError<any>('staticAnalysis'))
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
      return of(error);
    };
  }
}
