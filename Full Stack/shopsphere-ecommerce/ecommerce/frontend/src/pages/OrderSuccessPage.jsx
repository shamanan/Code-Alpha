import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../utils/api'
import { formatPrice, formatDate } from '../utils/helpers'
import { PageLoader } from '../components/common/Spinner'

export default function OrderSuccessPage() {
  const { id } = useParams()
  const [order, setOrder] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get(`/orders/${id}`)
      .then(r => setOrder(r.data))
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [id])

  if (loading) return <PageLoader />

  return (
    <div className="max-w-2xl mx-auto px-4 py-12">
      <div className="text-center mb-8">
        <div className="w-20 h-20 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg className="w-10 h-10 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Order Placed!</h1>
        <p className="text-gray-500">Thank you for your purchase. We'll send a confirmation shortly.</p>
      </div>

      {order && (
        <div className="bg-white rounded-2xl shadow-sm p-6 space-y-5">
          <div className="grid grid-cols-2 gap-4 text-sm">
            <div>
              <p className="text-gray-500">Order ID</p>
              <p className="font-mono font-semibold text-gray-800 text-xs mt-0.5">{order._id}</p>
            </div>
            <div>
              <p className="text-gray-500">Date</p>
              <p className="font-semibold text-gray-800 mt-0.5">{formatDate(order.createdAt)}</p>
            </div>
            <div>
              <p className="text-gray-500">Payment</p>
              <p className="font-semibold text-gray-800 mt-0.5">{order.paymentMethod}</p>
            </div>
            <div>
              <p className="text-gray-500">Status</p>
              <span className="inline-block mt-0.5 badge bg-yellow-100 text-yellow-800 capitalize">{order.status}</span>
            </div>
          </div>

          <div className="border-t border-gray-100 pt-4">
            <h3 className="font-semibold text-gray-800 mb-3">Items Ordered</h3>
            <div className="space-y-3">
              {order.orderItems.map((item, i) => (
                <div key={i} className="flex items-center gap-3 text-sm">
                  <img src={item.image} alt={item.name} className="w-12 h-12 rounded-lg object-cover bg-gray-100" />
                  <div className="flex-1">
                    <p className="font-medium text-gray-800">{item.name}</p>
                    <p className="text-gray-500">×{item.quantity} @ {formatPrice(item.price)}</p>
                  </div>
                  <p className="font-semibold">{formatPrice(item.price * item.quantity)}</p>
                </div>
              ))}
            </div>
          </div>

          <div className="border-t border-gray-100 pt-4 text-sm space-y-1">
            <div className="flex justify-between text-gray-600"><span>Subtotal</span><span>{formatPrice(order.itemsPrice)}</span></div>
            <div className="flex justify-between text-gray-600"><span>Shipping</span><span>{formatPrice(order.shippingPrice)}</span></div>
            <div className="flex justify-between text-gray-600"><span>Tax</span><span>{formatPrice(order.taxPrice)}</span></div>
            <div className="flex justify-between font-bold text-gray-900 text-base pt-1">
              <span>Total</span>
              <span className="text-blue-600">{formatPrice(order.totalPrice)}</span>
            </div>
          </div>

          <div className="border-t border-gray-100 pt-4 text-sm">
            <h3 className="font-semibold text-gray-800 mb-1">Shipping To</h3>
            <p className="text-gray-600">
              {order.shippingAddress.address}, {order.shippingAddress.city}, {order.shippingAddress.postalCode}, {order.shippingAddress.country}
            </p>
          </div>
        </div>
      )}

      <div className="flex gap-3 mt-6 justify-center">
        <Link to="/orders" className="btn-secondary">View All Orders</Link>
        <Link to="/products" className="btn-primary">Continue Shopping</Link>
      </div>
    </div>
  )
}
