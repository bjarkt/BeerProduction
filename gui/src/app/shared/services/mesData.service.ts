import { Injectable } from "@angular/core";
import {
  HttpClient,
  HttpHeaders,
  HttpErrorResponse
} from "@angular/common/http";
import { Observable, of } from "rxjs";
import { map, catchError, tap } from "rxjs/operators";
import { Order } from "../models/order";
import { environment } from "../../../environments/environment";
import { ProductionInfo } from "../models/ProductionInfo";

const MesEndpoint = environment.mesUrl;


const httpOptions = {
  headers: new HttpHeaders({
    "Content-Type": "application/json"
  })
};

@Injectable()
export class MesDataService {

    public loading: boolean;

    constructor(private http: HttpClient) { }

    private extractData(res: Response) {
        let body = res;
        return body || {};
    }

  
    public getMesOrders(): Observable<any> {
        return this.http.get(MesEndpoint + 'view-order-items', httpOptions);
    }

    public getMesOrderItems(orderNumber: number): Observable<any> {
        return this.http.get(MesEndpoint + 'view-order-items/' + orderNumber, httpOptions);
    }

    public viewStatistics(days?: number): Observable<any> {
        if (days) {
            return this.http.get(MesEndpoint + 'get-plant-statistics/?days=' + days, httpOptions);
        } else {
            return this.http.get(MesEndpoint + 'get-plant-statistics/', httpOptions)
        }
    }

    public getBatches(): Observable<any> {
        return this.http.get(MesEndpoint + 'view-all-batches', httpOptions)
    }
}
