import { LocalDateTime } from "./localdatetime";

export interface Order {
    orderNumber: number;
    status: string;
    date: LocalDateTime;
}