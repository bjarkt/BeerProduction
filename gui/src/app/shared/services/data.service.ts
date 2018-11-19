import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { map, catchError, tap } from 'rxjs/operators';
import { Order } from "../models/order";
import { environment } from '../../../environments/environment'
import { ProductionInfo } from "../models/ProductionInfo";

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

    public loading: boolean;

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
     * Delete order with given ID.
     * @param orderNumber order to delete.
     */
    public deleteOrder(orderNumber: number): Observable<any> {
        return this.http.post(ErpEndpoint + 'delete-order/' + orderNumber, null, httpOptions);
    }

    /**
     * 
     * @param orderNumber order details to retrieve
     */
    public getOrderDetails(orderNumber: number | string) {
        return this.http.get(ErpEndpoint + 'view-order-details/' + (+orderNumber)).pipe(map(res => res as Order));
    }

    public lockOrder(orderNumber: number): Observable<any>{
        return this.http.post(ErpEndpoint + 'lock-order/' + orderNumber, null, httpOptions);
    }

    /**
     * Add orderitem to order.
     * /add-order-item/:order-id/:beer-name/:quantity
     */
    public addOrderItem(orderNumber: number, beerName: string, quantity: number): Observable<any> {
        return this.http.post(ErpEndpoint + 'add-order-item/' + orderNumber + '/' + beerName + '/' + quantity, null, httpOptions);
    }

    /**
     * Get all order with given status.
     * /add-order-item/:order-id/:beer-name/:quantity
     * @param status order status
     */
    public getOrders(status: string): Observable<any> {
        return this.http.get(ErpEndpoint + 'view-orders/' + status);
    }

    public deleteOrderItem(orderNumber: number, beerName: string): Observable<any> {
        return this.http.post(ErpEndpoint + 'delete-order-item/' + orderNumber + '/' + beerName,null,httpOptions);
    }

    public getMesOrders(): Observable<any>{
        return this.http.get(MesEndpoint + 'view-orders', httpOptions);
    }

    /**
     * /get-orders
     */
    public getOrderItems(orderNumber: number): Observable<any>{
        return this.http.get(MesEndpoint + 'view-order-items/' + orderNumber, httpOptions);
    }

    /**
     * /create-batches
     */
    public createBatches(batches: ProductionInfo[]){
        const batchesMap = {orderItems: batches}
        console.log(batches)
        return this.http.post(MesEndpoint + 'create-batches', batchesMap, httpOptions );
    
    }






    public updateOrderItem(orderNumber: number, beerName: string, quantity: number): Observable<any>{
        return this.http.post(ErpEndpoint + 'edit-order-item/' + orderNumber + '/' + beerName + '?quantity=' + quantity,null,httpOptions);
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

    public getScadaLogsById(batchId: number): Observable<any> {
        return this.http.get(ScadaEndpoint + 'view-log/?batch-id=' + batchId);
    }

    public getScadaLogs(): Observable<any> {
        return this.http.get(ScadaEndpoint + 'view-log');
    }
}