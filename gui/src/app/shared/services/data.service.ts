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

    /**
     * Create new order and retrieve order number.
     */
    public createOrder(): Observable<any> {
        return this.http.post(ErpEndpoint + 'create-order', null, httpOptions);
    }

    /**
     * /add-order-item/:order-id/:beer-name/:quantity
     */
    public addOrderItem(orderNumber: number, beerName: string, quantity: number): Observable<any>{
        return this.http.post(ErpEndpoint + 'add-order-item/' + orderNumber + '/' + beerName + '/' + quantity, null, httpOptions);
    }


    // *******************************
    // **********   SCADA   **********
    // *******************************
    public viewScreen(): Observable<any> {
        return this.http.get(ScadaEndpoint + 'view-screen');
    }

    public manageProduction(action: string): Observable<any> {
        return this.http.post(ScadaEndpoint + 'manage-production/' + action, null, httpOptions);
    }
}