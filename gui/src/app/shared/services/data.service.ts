import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { map, catchError, tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment'

const ErpEndpoint = environment.erpUrl;
const MesEndpoint = environment.mesUrl;
const ScadaEndpoint = environment.scadaUrl;

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

    public viewOrderItems(orderId: number): Observable<any> {
        return this.http.get(ErpEndpoint + 'view-order-items/' + orderId);
    }


}
