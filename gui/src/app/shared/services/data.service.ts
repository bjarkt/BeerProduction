import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { map, catchError, tap } from 'rxjs/operators';

const ErpEndpoint = 'http://localhost:7002/api/';
const MesEndpoint = 'http://localhost:7001/api/';
const ScadaEndpoint = 'http://localhost:7000/api/';
const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json'
    })
};

@Injectable()
export class DataService {

    constructor(private http: HttpClient) { }

    private extractData(res: Response) {
        let body = res;
        return body || {};
    }

    public createOrder(): Observable<any> {
        return this.http.post<any>(ErpEndpoint + 'create-order', httpOptions);
    }


}