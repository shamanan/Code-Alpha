import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import api from '../utils/api'
import { useAuth } from './AuthContext'
import toast from 'react-hot-toast'

const CartContext = createContext()

export const CartProvider = ({ children }) => {
  const [cart, setCart] = useState({ items: [] })
  const [loading, setLoading] = useState(false)
  const { user } = useAuth()

  const fetchCart = useCallback(async () => {
    if (!user) { setCart({ items: [] }); return }
    try {
      setLoading(true)
      const { data } = await api.get('/cart')
      setCart(data)
    } catch (err) {
      console.error('Fetch cart error:', err)
    } finally {
      setLoading(false)
    }
  }, [user])

  useEffect(() => { fetchCart() }, [fetchCart])

  const addToCart = async (productId, quantity = 1) => {
    if (!user) { toast.error('Please login to add items to cart'); return }
    try {
      const { data } = await api.post('/cart', { productId, quantity })
      setCart(data)
      toast.success('Added to cart!')
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to add to cart')
    }
  }

  const updateQuantity = async (productId, quantity) => {
    try {
      const { data } = await api.put(`/cart/${productId}`, { quantity })
      setCart(data)
    } catch (err) {
      toast.error('Failed to update quantity')
    }
  }

  const removeFromCart = async (productId) => {
    try {
      const { data } = await api.delete(`/cart/${productId}`)
      setCart(data)
      toast.success('Item removed from cart')
    } catch (err) {
      toast.error('Failed to remove item')
    }
  }

  const clearCart = async () => {
    try {
      await api.delete('/cart')
      setCart({ items: [] })
    } catch (err) {
      console.error('Clear cart error:', err)
    }
  }

  const cartCount = cart.items?.reduce((sum, item) => sum + item.quantity, 0) || 0
  const cartTotal = cart.items?.reduce((sum, item) => {
    const price = item.product?.price || 0
    return sum + price * item.quantity
  }, 0) || 0

  return (
    <CartContext.Provider value={{
      cart, loading, cartCount, cartTotal,
      addToCart, updateQuantity, removeFromCart, clearCart, fetchCart
    }}>
      {children}
    </CartContext.Provider>
  )
}

export const useCart = () => {
  const ctx = useContext(CartContext)
  if (!ctx) throw new Error('useCart must be used within CartProvider')
  return ctx
}
