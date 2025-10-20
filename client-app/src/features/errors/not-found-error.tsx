import { useNavigate, useRouter } from '@tanstack/react-router'
import { useAuthStore } from '@/stores/auth-store'
import { Button } from '@/components/ui/button'

export function NotFoundError() {
  const navigate = useNavigate()
  const { history } = useRouter()
  const { user } = useAuthStore()

  const handleBackToHome = () => {
    // If user is authenticated, go to dashboard, otherwise go to root
    const homePath = user ? '/' : '/sign-in'
    navigate({ to: homePath })
  }

  return (
    <div className='h-svh'>
      <div className='m-auto flex h-full w-full flex-col items-center justify-center gap-2'>
        <h1 className='text-[7rem] leading-tight font-bold'>404</h1>
        <span className='font-medium'>Oops! Page Not Found!</span>
        <p className='text-muted-foreground text-center'>
          It seems like the page you're looking for <br />
          does not exist or might have been removed.
        </p>
        <div className='mt-6 flex gap-4'>
          <Button variant='outline' onClick={() => history.go(-1)}>
            Go Back
          </Button>
          <Button onClick={handleBackToHome}>
            Back to {user ? 'Dashboard' : 'Home'}
          </Button>
        </div>
      </div>
    </div>
  )
}
