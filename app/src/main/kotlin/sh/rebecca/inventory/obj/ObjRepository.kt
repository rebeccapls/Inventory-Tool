package sh.rebecca.inventory.obj

import com.displee.cache.CacheLibrary
import io.Buffer
import org.springframework.stereotype.Component
import sh.rebecca.inventory.repository.Repository

interface ObjRepository : Repository<Obj>

@Component
class CacheObjRepository(private val reader: ObjReader, private val cache: CacheLibrary) : ObjRepository {

    private val objects: List<Obj>

    init {
        val idxBuffer = Buffer(cache.data(0, 2, "obj.idx")!!)
        val dataBuffer = Buffer(cache.data(0, 2, "obj.dat")!!)

        objects = (0 until idxBuffer.readUShort()).map { id ->
            reader.read(dataBuffer, id)
        }.toList()

    }

    override fun findById(id: Int): Obj? {
        return objects.getOrNull(id)
    }

    override fun getCount(): Int {
        return objects.size
    }
}
