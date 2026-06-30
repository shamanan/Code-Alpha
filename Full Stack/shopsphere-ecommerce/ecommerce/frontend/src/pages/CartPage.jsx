import { Link, useNavigate } from 'react-router-dom'
import { useCart } from '../context/CartContext'
import CartItem from '../components/cart/CartItem'
import { PageLoader } from '../components/common/Spinner'
import { formatPrice } from '../utils/helpers'

export default function CartPage() {
  const { cart, loading, cartTotal, cartCount } = useCart()
  const navigate = useNavigate()

  const shipping = cartTotal > 50 ? 0 : 5.99
  const tax = cartTotal * 0.1
  const total = cartTotal + shipping + tax

  if (loading) return <PageLoader />

  const isEmpty = !cart.items || cart.items.length === 0

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">Shopping Cart</h1>

      {isEmpty ? (
        <div className="text-center py-20">
          <div className="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg className="w-12 h-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M3 3h2l.4 2M7 13h10l4-8H5.4m1.6 8a2 2 0 100 4 2 2 0 000-4zm10 0a2 2 0 100 4 2 2 0 000-4z" />
            </svg>
          </div>
          <h2 className="text-xl font-semibold text-gray-700 mb-2">Your cart is empty</h2>
          <p className="text-gray-500 mb-6">Looks like you haven't added anything yet.</p>
          <Link to="/products" className="btn-primary">Start Shopping</Link>
        </div>
      ) : (
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Items */}
          <div className="flex-1">
            <div className="bg-white rounded-2xl shadow-sm p-6">
              <p className="text-sm text-gray-500 mb-2">{cartCount} item{cartCount !== 1 ? 's' : ''}</p>
              {cart.items.map((item) => (
                <CartItem key={item.product?._id || item._id} item={item} />
              ))}
            </div>
            <div className="mt-4">
              <Link to="/products" className="text-blue-600 hover:underline text-sm font-medium">
                ← Continue Shopping
              </Link>
            </div>
          </div>

          {/* Summary */}
          <div className="lg:w-80 shrink-0">
            <div className="bg-white rounded-2xl shadow-sm p-6 sticky top-24">
              <h2 className="text-lg font-bold text-gray-900 mb-4">Order Summary</h2>
              <div className="space-y-3 text-sm mb-4">
                <div className="flex justify-between text-gray-600">
                  <span>Subtotal ({cartCount} items)</span>
                  <span className="font-medium">{formatPrice(cartTotal)}</span>
                </div>
                <div className="flex justify-between text-gray-600">
                  <span>Shipping</span>
                  <span className="font-medium">
                    {shipping === 0 ? <span className="text-green-600">Free</span> : formatPrice(shipping)}
                  </span>
                </div>
                <div className="flex justify-between text-gray-600">
                  <span>Tax (10%)</span>
                  <span className="font-medium">{formatPrice(tax)}</span>
                </div>
              </div>
              {cartTotal < 50 && (
                <div className="bg-yellow-50 text-yellow-800 text-xs rounded-lg p-2 mb-4">
                  Add {formatPrice(50 - cartTotal)} more for free shipping!
                </div>
              )}
              <div className="border-t border-gray-100 pt-3 mb-5">
                <div className="flex justify-between font-bold text-gray-900">
                  <span>Total</span>
                  <span className="text-blue-600 text-lg">{formatPrice(total)}</span>
                </div>
              </div>
              <button
                onClick={() => navigate('/checkout')}
                className="btn-primary w-full text-center"
              >
                Proceed to Checkout
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
