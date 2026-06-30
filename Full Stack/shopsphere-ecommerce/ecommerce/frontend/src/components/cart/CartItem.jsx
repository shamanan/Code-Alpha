import { Link } from 'react-router-dom'
import { useCart } from '../../context/CartContext'
import { formatPrice } from '../../utils/helpers'

export default function CartItem({ item }) {
  const { updateQuantity, removeFromCart } = useCart()
  const { product, quantity } = item

  if (!product) return null

  return (
    <div className="flex items-start gap-4 py-5 border-b border-gray-100 last:border-0">
      <Link to={`/products/${product._id}`} className="shrink-0">
        <img
          src={product.image}
          alt={product.name}
          className="w-20 h-20 object-cover rounded-lg bg-gray-100"
          onError={(e) => { e.target.src = 'https://via.placeholder.com/80?text=IMG' }}
        />
      </Link>
      <div className="flex-1 min-w-0">
        <Link to={`/products/${product._id}`}>
          <h4 className="font-semibold text-gray-900 hover:text-blue-600 transition-colors line-clamp-2">{product.name}</h4>
        </Link>
        <p className="text-sm text-gray-500 mt-0.5">{product.brand}</p>
        <p className="font-bold text-gray-900 mt-1">{formatPrice(product.price)}</p>
      </div>
      <div className="flex flex-col items-end gap-2 shrink-0">
        {/* Quantity controls */}
        <div className="flex items-center gap-1 border border-gray-200 rounded-lg overflow-hidden">
          <button
            onClick={() => updateQuantity(product._id, quantity - 1)}
            disabled={quantity <= 1}
            className="w-8 h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
          >−</button>
          <span className="w-8 text-center text-sm font-semibold">{quantity}</span>
          <button
            onClick={() => updateQuantity(product._id, quantity + 1)}
            disabled={quantity >= product.countInStock}
            className="w-8 h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
          >+</button>
        </div>
        <p className="text-sm font-bold text-blue-600">{formatPrice(product.price * quantity)}</p>
        <button
          onClick={() => removeFromCart(product._id)}
          className="text-red-400 hover:text-red-600 transition-colors"
          title="Remove item"
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        </button>
      </div>
    </div>
  )
}
