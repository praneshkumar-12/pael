import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, map, switchMap, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  baseUrl = 'http://localhost:8080/api/v1';
  constructor(private http: HttpClient) { }

  getAccount(): Observable<any> {
    return this.http.get<any[]>(`${this.baseUrl}/accounts`).pipe(
      map(accounts => accounts && accounts.length > 0 ? accounts[0] : null)
    );
  }

  getBalance(): Observable<any> {
    return this.getAccount().pipe(
      map(account => {
        if (account) return { balance: account.balance };
        return { balance: 0 };
      })
    );
  }

  transfer(data: any): Observable<any> {
    const transferPayload = {
      toAccountId: Number(data.toAccount),
      amount: data.amount,
      idempotencyKey: crypto.randomUUID()
    };

    return this.getAccount().pipe(
      switchMap(account => {
        if (!account) throw new Error('No account found');
        return this.http.post(`${this.baseUrl}/transfers`, {
          ...transferPayload,
          fromAccountId: account.id
        });
      })
    );
  }

  getHistory(): Observable<any> {
    return this.getAccount().pipe(
      switchMap(account => {
        if (!account) return of([]);
        return this.http.get(`${this.baseUrl}/accounts/${account.id}/transactions`);
      })
    );
  }

  // Reward API endpoints
  getRewardSummary(): Observable<any> {
    return this.http.get(`${this.baseUrl}/rewards/summary`);
  }

  getRewardHistory(): Observable<any> {
    return this.http.get(`${this.baseUrl}/rewards/history`);
  }

  lookupAccount(accountId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/accounts/${accountId}`);
  }
}
