export type AuthResponse = { token: string; username: string }
export type ChatMessage = { id: number; conversationId: string; sender: string; content: string; createdAt: string }