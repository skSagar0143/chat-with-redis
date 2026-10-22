import type { AuthResponse, ChatMessage } from './types'
const API = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'
async function request<T>(path: string, init: RequestInit = {}, token?: string): Promise<T> {
 const response = await fetch(`${API}${path}`, { ...init, headers: { 'Content-Type': 'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}), ...init.headers } })
 if (!response.ok) { const body = await response.text(); throw new Error(body || `Request failed (${response.status})`) }
 return response.json() as Promise<T>
}
export const api = {
 register: (username: string, password: string) => request<AuthResponse>('/auth/register', { method: 'POST', body: JSON.stringify({ username, password }) }),
 login: (username: string, password: string) => request<AuthResponse>('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) }),
 history: (room: string, token: string) => request<ChatMessage[]>(`/chat/${encodeURIComponent(room)}`, {}, token),
 onlineUsers: (token: string) => request<string[]>('/presence/online', {}, token),
 heartbeat: (token: string) => request<{status:string}>('/presence/heartbeat', { method: 'POST' }, token),
}